package edu.utfpr.controller;

import edu.utfpr.model.Animal;
import edu.utfpr.model.Profissional;
import edu.utfpr.repository.AnimalRepository;
import edu.utfpr.repository.ProfissionalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProfissionalController {

    @Autowired
    ProfissionalRepository profissionalRepository;
    @Autowired
    AnimalRepository animalRepository;

    @GetMapping("/profissional/cadastrar")
    public String cadastrar(Model model){
        model.addAttribute("profissional", new Profissional());
        return"profissional/cadastrar";
    }

    @PostMapping("/profissional/salvar") // Utilizado para inclusão de um novo registro
    public String salvar(@ModelAttribute Profissional profissional){
        profissionalRepository.salvar(profissional);
        return"redirect:/";
    }

    @PostMapping("/profissional/salvar/{id}") // Utilizado para editar um registro já existente
    public String editar(@ModelAttribute Profissional profissional, @PathVariable String id){
        ObjectId profissionalid = new ObjectId(id);
        profissional.setProfissionalid(profissionalid);

        profissionalRepository.salvar(profissional);
        return"redirect:/";
    }

    @GetMapping("/profissional/listar")
    public String listar(Model model){
        List<Profissional> profissionais = profissionalRepository.listarTodos();
        model.addAttribute("profissionais", profissionais);
        return"profissional/listar";
    }

    @GetMapping("/profissional/visualizar/{id}")
    public String visualizar(@PathVariable String id, Model model){

        Profissional profissional = profissionalRepository.obterId(id);
        model.addAttribute("profissional", profissional);

        List<Animal> animais = animalRepository.buscarPorProfissional(id);
        model.addAttribute("animais", animais);

        return "profissional/visualizar";
    }

    @GetMapping("/profissional/excluir/{id}")
    public String excluir(@PathVariable String id){
        profissionalRepository.excluir(id);
        return "redirect:/profissional/listar";
    }
}
