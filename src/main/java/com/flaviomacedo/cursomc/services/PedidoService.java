package com.flaviomacedo.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.flaviomacedo.cursomc.domain.Cliente;
import com.flaviomacedo.cursomc.domain.ItemPedido;
import com.flaviomacedo.cursomc.domain.PagamentoComBoleto;
import com.flaviomacedo.cursomc.domain.Pedido;
import com.flaviomacedo.cursomc.domain.enums.EstadoPagamento;
import com.flaviomacedo.cursomc.repositories.ItemPedidoRepository;
import com.flaviomacedo.cursomc.repositories.PagamentoRepository;
import com.flaviomacedo.cursomc.repositories.PedidoRepository;
import com.flaviomacedo.cursomc.security.UserSS;
import com.flaviomacedo.cursomc.services.exceptions.AuthorizationException;
import com.flaviomacedo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired 
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido objeto) {
		objeto.setId(null);
		objeto.setInstante(new Date());
		objeto.setCliente(clienteService.find(objeto.getCliente().getId()));
		objeto.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		objeto.getPagamento().setPedido(objeto);
		if (objeto.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto boleto = (PagamentoComBoleto) objeto.getPagamento();
			boletoService.preencherPagamentoComBoleto(boleto, objeto.getInstante());
		}
		objeto = repository.save(objeto);
		pagamentoRepository.save(objeto.getPagamento());
		for (ItemPedido itemPedido : objeto.getItens()) {
			itemPedido.setDesconto(0.0);
			itemPedido.setProduto(produtoService.find(itemPedido.getProduto().getId()));
			itemPedido.setPreco(itemPedido.getProduto().getPreco());
			itemPedido.setPedido(objeto);
		}
		itemPedidoRepository.saveAll(objeto.getItens());
		emailService.sendOrderConfirmationEmail(objeto);
		return objeto;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.find(user.getId());
		return repository.findByCliente(cliente, pageRequest);
	}
}
