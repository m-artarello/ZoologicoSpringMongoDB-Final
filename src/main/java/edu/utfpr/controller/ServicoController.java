package edu.utfpr.controller;

import edu.utfpr.model.*;
import edu.utfpr.repository.AnimalRepository;
import edu.utfpr.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ServicoController {
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    ProfissionalRepository profissionalRepository;

    @GetMapping("/servico/cadastrar/{id}")
    public String cadastrar(@PathVariable String id, Model model){
        Animal animal = animalRepository.obterId(id);
        List<Servico> servicos = List.of(Servico.values());
        List<Profissional> profissionais = profissionalRepository.listarTodos();

        model.addAttribute("servicos", servicos);
        model.addAttribute("animal", animal);
        model.addAttribute("profissionais", profissionais);
        model.addAttribute("servico", new ServicoRealizado());
        return"servico/cadastrar";
    }

    @PostMapping("/servico/salvar/{id}")
    public String salvar(@PathVariable String id,
                         @RequestParam("servicoDescricao") String servicoDescricao,
                         @RequestParam("treinadorid") String treinadorIdStr,
                         @RequestParam("data") String data,
                         @RequestParam("hora") String hora){

        Animal animal = animalRepository.obterId(id);
        Profissional profissional = profissionalRepository.obterId(treinadorIdStr);

        animal.addServicoRealizado(servicoDescricao, profissional, data, hora);
        animalRepository.salvar(animal);
        return"redirect:/";
    }
}
