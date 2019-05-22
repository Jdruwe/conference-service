package be.xplore.conference.scheduler;

import be.xplore.conference.listener.ClientEventListener;
import be.xplore.conference.model.Client;
import be.xplore.conference.service.ClientService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ClientScheduler {

    private final ClientService clientService;
    private final ScheduledExecutorService scheduler;
    private final List<ClientEventListener> listeners;

    private ScheduledFuture<?> scheduledFuture;

    public ClientScheduler(ClientService clientService, List<ClientEventListener> listeners) {
        this.clientService = clientService;
        this.listeners = listeners;
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
        List<Client> offlineClients = clientService.loadOfflineClients();
        notifyListeners(offlineClients);
    }

    private void notifyListeners(List<Client> clients) {
        listeners.forEach(l -> l.onOfflineClients(clients));
    }
}
