package com.refaccionaria.sistemapos.cliente;
import com.refaccionaria.sistemapos.producto.Producto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ClienteService
{
    private ClienteRepository clienteRepository;
    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository=clienteRepository;
    }
    //agregar cliente
    public Cliente AgregarCliente(Cliente cliente){
        return clienteRepository.save(cliente);

    }
    //modificar cliente
    public Cliente ModificarCliente( Cliente clienteNuevo){
        Cliente cliente = clienteRepository.findById(clienteNuevo.getIdcliente()).orElseThrow();
        cliente.setNombre(clienteNuevo.getNombre());
        cliente.setDireccion(clienteNuevo.getDireccion());
        cliente.setTipo_cliente(clienteNuevo.getTipo_cliente());
        cliente.setTelefono(clienteNuevo.getTelefono());
        return clienteRepository.save(cliente);
    }
    //eliminar cliente

    public Cliente EliminarClienteById(Integer id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setActivo(false);
        return clienteRepository.save(cliente);
    }
    //buscar cliente
    public Cliente BuscarById(Integer id){
        return clienteRepository.findById(id).orElseThrow();
    }

    public List<Cliente> ListarClientes(){
        return clienteRepository.findAll();
    }




}
