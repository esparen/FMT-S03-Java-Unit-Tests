package com.example.demo.service;

import com.example.demo.database.entities.Estudante;
import com.example.demo.database.repositories.EstudanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstudanteServiceTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @InjectMocks
    private EstudanteService estudanteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarEstudante() {
        Estudante estudanteMock = new Estudante();
        estudanteMock.setNome("João");
        estudanteMock.setMatricula("12345");

        when(estudanteRepository.save(any(Estudante.class))).thenReturn(estudanteMock);

        Estudante estudante = estudanteService.cadastrarEstudante("João", "12345");

        assertNotNull(estudante);
        assertEquals("João", estudante.getNome());
        assertEquals("12345", estudante.getMatricula());
        verify(estudanteRepository, times(1)).save(any(Estudante.class));
    }

    @Test
    public void testListarEstudantes() {
        List<Estudante> estudantesMock = new ArrayList<>();
        estudantesMock.add(new Estudante());
        estudantesMock.add(new Estudante());

        when(estudanteRepository.findAll()).thenReturn(estudantesMock);

        List<Estudante> estudantes = estudanteService.listarEstudantes();

        assertNotNull(estudantes);
        assertEquals(2, estudantes.size());
        verify(estudanteRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarEstudantePorIdFound() {
        Estudante estudanteMock = new Estudante();
        estudanteMock.setId(1L);
        estudanteMock.setNome("João");

        when(estudanteRepository.findById(anyLong())).thenReturn(Optional.of(estudanteMock));

        Estudante estudante = estudanteService.buscarEstudantePorId(1L);

        assertNotNull(estudante);
        assertEquals(1L, estudante.getId());
        assertEquals("João", estudante.getNome());
        verify(estudanteRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testBuscarEstudantePorIdNotFound() {
        when(estudanteRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estudanteService.buscarEstudantePorId(1L);
        });

        assertEquals("Estudante não encontrado", exception.getMessage());
        verify(estudanteRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testAtualizarEstudante() {
        Estudante estudanteMock = new Estudante();
        estudanteMock.setId(1L);
        estudanteMock.setNome("João");
        estudanteMock.setMatricula("12345");

        when(estudanteRepository.findById(anyLong())).thenReturn(Optional.of(estudanteMock));
        when(estudanteRepository.save(any(Estudante.class))).thenReturn(estudanteMock);

        Estudante estudanteAtualizado = estudanteService.atualizarEstudante(1L, "Carlos", "54321");

        assertNotNull(estudanteAtualizado);
        assertEquals("Carlos", estudanteAtualizado.getNome());
        assertEquals("54321", estudanteAtualizado.getMatricula());
        verify(estudanteRepository, times(1)).findById(anyLong());
        verify(estudanteRepository, times(1)).save(any(Estudante.class));
    }

    @Test
    public void testRemoverEstudante() {
        when(estudanteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(estudanteRepository).deleteById(anyLong());
        estudanteService.removerEstudante(1L);

        verify(estudanteRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testRemoverEstudanteNotFound() {
        when(estudanteRepository.existsById(anyLong())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estudanteService.removerEstudante(1L);
        });

        assertEquals("Estudante não encontrado", exception.getMessage());
        verify(estudanteRepository, times(1)).existsById(anyLong());
        verify(estudanteRepository, never()).deleteById(anyLong());
    }
}
