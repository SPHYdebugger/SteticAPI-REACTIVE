package com.home.SteticApi.service;

import com.home.SteticApi.domain.Client;

import com.home.SteticApi.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(long id) {
        return clientRepository.findById(id);
    }


    public List<Client> findClientByFirstname(String firstname) {
        return clientRepository.findByFirstname(firstname);
    }
    public Optional<Client> findClientByDni(String dni){ return  clientRepository.findClientByDni(dni);}
    public List<Client> findClientByCity(String city){ return clientRepository.findByCity(city);}
    public List<Client> findClientByFirstnameAndDNI(String firstname, String dni) {
        return clientRepository.findByFirstnameAndDni(firstname, dni);
    }

    public void saveClient(Client client) { clientRepository.save(client);
    }
    @Transactional
    public void removeClient(String dni) {
        clientRepository.deleteByDni(dni);
    }

    @Transactional
    public void removeClientById(long clientId) {
        clientRepository.deleteById(clientId);
    }

    public void modifyClient(Client newClient, String dni) {
        Optional<Client> client = clientRepository.findClientByDni(dni);
        if (client.isPresent()) {
            Client existingClient = client.get();
            existingClient.setFirstname(newClient.getFirstname());
            existingClient.setLastname(newClient.getLastname());
            existingClient.setCity(newClient.getCity());
            existingClient.setStreet(newClient.getStreet());
            existingClient.setNumHouse(newClient.getNumHouse());
            existingClient.setAge(newClient.getAge());
            clientRepository.save(existingClient);
        }
    }

    public void modifyClientById(Client newClient, long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            Client existingClient = client.get();
            existingClient.setFirstname(newClient.getFirstname());
            existingClient.setLastname(newClient.getLastname());
            existingClient.setCity(newClient.getCity());
            existingClient.setStreet(newClient.getStreet());
            existingClient.setNumHouse(newClient.getNumHouse());
            existingClient.setAge(newClient.getAge());
            clientRepository.save(existingClient);
        }
    }
}
