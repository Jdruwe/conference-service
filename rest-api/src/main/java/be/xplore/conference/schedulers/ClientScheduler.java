package be.xplore.conference.schedulers;

import be.xplore.conference.Client;
import be.xplore.conference.notifications.EmailSender;
import be.xplore.conference.service.ClientService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ClientScheduler {

    private final ClientService clientService;
    private final EmailSender emailSender;
    private final ScheduledExecutorService scheduler;

    private ScheduledFuture<?> scheduledFuture;

    private List<Client> offlineClients = new ArrayList<>();


    public ClientScheduler(ClientService clientService, EmailSender emailSender) {
        this.clientService = clientService;
        this.emailSender = emailSender;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @PostConstruct
    public void startClientScheduler() {
        startScheduler("30");
    }

    public void startScheduler(String time) {
        Runnable task2 = this::checkStatusClientsAndSendMail;
        scheduledFuture = scheduler.scheduleAtFixedRate(
                task2,
                0,
                Long.parseLong(time),
                TimeUnit.MINUTES);
    }

    public void stopScheduler() {
        scheduledFuture.cancel(true);
    }

    private void checkStatusClientsAndSendMail() {
        List<Client> currentClients = clientService.loadAll();
        if (!currentClients.isEmpty()) { //&& offlineClients != null
            List<Client> checkedAllClientsOnConnectivity = checkAllClientsConnectivity(currentClients);
            offlineClients = updateOfflineClients(offlineClients, currentClients);
            if (!checkedAllClientsOnConnectivity.isEmpty()) {
                sendMail(getCurrentOfflineClients(offlineClients, checkedAllClientsOnConnectivity));
            }
        }
    }

    private List<Client> checkAllClientsConnectivity(List<Client> currentClients) {
        return currentClients
                .stream()
                .filter(c -> ChronoUnit.MILLIS.between(c.getLastConnected(), LocalDateTime.now()) > 180_000)
                .collect(Collectors.toList());
    }

    private List<Client> updateOfflineClients(List<Client> offlineClients, List<Client> currentClients) {
        return findDuplicatesInClientListsById(offlineClients, currentClients);

    }

    private List<Client> getCurrentOfflineClients(List<Client> offlineClients, List<Client> checkedAllClientsOnConnectivity) {
        List<Client> duplicatesToRemove = findDuplicatesInClientListsById(offlineClients, checkedAllClientsOnConnectivity);
        checkedAllClientsOnConnectivity.removeAll(duplicatesToRemove);
        offlineClients.addAll(checkedAllClientsOnConnectivity);
        return offlineClients;
    }

    private List<Client> findDuplicatesInClientListsById(List<Client> firstClientList, List<Client> secondClientList) {
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
        emailSender.sendEmailForOfflineClients(clients);
    }

    public boolean wasClientOffline(Client clientToCheck) {
        return offlineClients.stream().anyMatch(c -> c.getId() == clientToCheck.getId());
    }
}
