package be.xplore.conference.schedulers;

import be.xplore.conference.model.Client;
import be.xplore.conference.notifications.EmailSender;
import be.xplore.conference.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientCheckerScheduler {
    private static final Logger log = LoggerFactory.getLogger(ClientCheckerScheduler.class);

    private ClientService clientService;
    private EmailSender emailSender;

    private List<Client> offlineClients = new ArrayList<>();


    public ClientCheckerScheduler(ClientService clientService, EmailSender emailSender) {
        this.clientService = clientService;
        this.emailSender = emailSender;
    }

    //every 30 minutes 180 000
    @Scheduled(fixedRate = 180000)
    private void checkStatusClientsAndSendMail() {
        List<Client> currentClients = clientService.loadAll();
        if (currentClients.size() != 0 && offlineClients != null) {
            List<Client> checkedAllClientsOnConnectivity = checkAllClientsConnectivity(currentClients);
            offlineClients = updateOfflineClients(offlineClients, currentClients);
            sendMail(getCurrentOfflineClients(offlineClients, checkedAllClientsOnConnectivity));
        }
    }

    private List<Client> checkAllClientsConnectivity(List<Client> currentClients) {
        return currentClients
                .stream()
                .filter(c -> new Date().getTime() - c.getLastConnected().getTime() > 180000)
                .collect(Collectors.toList());
    }

    private List<Client> updateOfflineClients(List<Client> offlineClients, List<Client> currentClients) {
        return findDuplicatesInClientListsById(offlineClients,currentClients);

    }

    private List<Client> getCurrentOfflineClients(List<Client> offlineClients, List<Client> checkedAllClientsOnConnectivity) {
        List<Client> duplicatesToRemove = findDuplicatesInClientListsById(offlineClients,checkedAllClientsOnConnectivity);
        checkedAllClientsOnConnectivity.removeAll(duplicatesToRemove);
        offlineClients.addAll(checkedAllClientsOnConnectivity);
        return offlineClients;
    }

    private List<Client> findDuplicatesInClientListsById(List<Client> firstClientList, List<Client> secondClientList){
        List<Client> duplicates = new ArrayList<>();
        for (Client firstClient : firstClientList) {
            for (Client secondClient : secondClientList) {
                if (firstClient.getId() == secondClient.getId()) {
                    duplicates.add(secondClient);
                }
            }
        }
        return duplicates;
    }

    private void sendMail(List<Client> clients) {
        emailSender.sendEmail(clients);
        log.info("email send!");
    }
}
