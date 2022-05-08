package com.dio.santander.banklineapi.service;

import com.dio.santander.banklineapi.dto.MovimentacaoDTO;
import com.dio.santander.banklineapi.model.Correntista;
import com.dio.santander.banklineapi.model.Movimentacao;
import com.dio.santander.banklineapi.model.MovimentacaoTipo;
import com.dio.santander.banklineapi.repository.CorrentistaRepository;
import com.dio.santander.banklineapi.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private CorrentistaRepository correntistaRepository;
    public void save(MovimentacaoDTO novaMovimentacao) {
        Movimentacao movimentacao = new Movimentacao();

        Double valor = novaMovimentacao.getTipo() == MovimentacaoTipo.RECEITA
                ? novaMovimentacao.getValor() : novaMovimentacao.getValor() * -1;
        movimentacao.setDatahora(LocalDateTime.now());
        movimentacao.setDescricao(novaMovimentacao.getDescricao());
        movimentacao.setidConta(novaMovimentacao.getIdConta());
        movimentacao.setTipo(novaMovimentacao.getTipo());
        movimentacao.setValor(valor);

        Correntista correntista = correntistaRepository.findById(
                novaMovimentacao.getIdConta()).orElse(null);

        if (correntista != null) {
            correntista.getConta().setSaldo(
                    correntista.getConta().getSaldo() + valor);
            correntistaRepository.save(correntista);
        }
        repository.save(movimentacao);
    }
}
