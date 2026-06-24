package com.refaccionaria.sistemapos.cliente;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/Clientes/{idCliente}")
    public Cliente BuscarClienteById(@PathVariable Integer id){
        return clienteService.BuscarById(id);
    }







}
