package be.xplore.conference.listener;

import be.xplore.conference.model.Client;

import java.util.List;

public interface ClientEventListener {
    void onOfflineClients(List<Client> offlineClients);

    void onReconnectedClient(Client reconnectedClient);
}
