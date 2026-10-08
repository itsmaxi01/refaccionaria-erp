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
    public List<ClienteDTO> ListarClientes() {
        return clienteService.ListarClientes().stream()
                .map(ClienteDTO::fromEntity)
                .toList();
    }

    @PostMapping
    public ClienteDTO AgregarCliente(@RequestBody ClienteDTO cliente) {
        return ClienteDTO.fromEntity(clienteService.AgregarCliente(cliente.toEntity()));
    }

    @DeleteMapping("/{id}")
    public ClienteDTO EliminarCliente(@PathVariable Integer id) {
        return ClienteDTO.fromEntity(clienteService.EliminarClienteById(id));
    }
    @PutMapping
    public ClienteDTO ModificarCliente(@RequestBody ClienteDTO cliente){
        return ClienteDTO.fromEntity(clienteService.ModificarCliente(cliente.toEntity()));
    }

    @GetMapping("/Clientes/{idCliente}")
    public ClienteDTO BuscarClienteById(@PathVariable Integer idCliente) {
        return ClienteDTO.fromEntity(clienteService.BuscarById(idCliente));
    }







}
