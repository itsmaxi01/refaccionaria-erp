package com.refaccionaria.sistemapos.cliente;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@RestController
@RequestMapping("/Clientes")
public class ClienteController {

    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> ListarClientes() {
        return clienteService.ListarClientes();
    }

    @PostMapping
    public Cliente AgregarCliente(@RequestBody Cliente cliente) {
        System.out.println("====== REQUEST ======");
        System.out.println("ID: " + cliente.getIdCliente());
        System.out.println("Nombre: " + cliente.getNombre());
        System.out.println("Tipo: " + cliente.getTipoCliente());



        return clienteService.AgregarCliente(cliente);
    }

    @DeleteMapping("/{id}")
    public Cliente EliminarCliente(@PathVariable Integer id) {
        return clienteService.EliminarClienteById(id);
    }
    @PutMapping
    public Cliente ModificarCliente(@RequestBody Cliente cliente){
        return clienteService.ModificarCliente(cliente);
    }

    @GetMapping("/Clientes/{idCliente}")
    public Cliente BuscarClienteById(@PathVariable Integer idCliente) {
        return clienteService.BuscarById(idCliente);
    }







}
