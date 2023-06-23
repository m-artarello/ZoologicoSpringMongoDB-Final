package edu.utfpr.controller;

import edu.utfpr.model.Animal;
import edu.utfpr.model.Profissional;
import edu.utfpr.repository.AnimalRepository;
import edu.utfpr.repository.ProfissionalRepository;
import edu.utfpr.service.ServicoReport;
import net.sf.jasperreports.engine.JRException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@Controller
public class AnimalController {

    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    ProfissionalRepository profissionalRepository;
    @Autowired
    ServicoReport report;

    @GetMapping("/animal/cadastrar")
    public String cadastrar(Model model){
        List<Profissional> profissionais = profissionalRepository.listarTodos();
        model.addAttribute("animal", new Animal());
        model.addAttribute("profissionais", profissionais);
        return"animal/cadastrar";
    }

    @PostMapping("/animal/salvar") // Utilizado para inclusão de um novo registro
    public String salvar(@ModelAttribute Animal animal, @RequestParam("treinadorid") String treinadoridStr){
        Profissional treinador = profissionalRepository.obterId(treinadoridStr);
        animal.setTreinador(treinador);

        animalRepository.salvar(animal);
        return"redirect:/";
    }

    @PostMapping("/animal/salvar/{id}") // Utilizado para editar um registro já existente
    public String editar(@ModelAttribute Animal animal, @RequestParam("treinadorid") String treinadoridStr, @PathVariable String id){
        Profissional treinador = profissionalRepository.obterId(treinadoridStr);
        ObjectId animalid = new ObjectId(id);
        animal.setTreinador(treinador);
        animal.setAnimalid(animalid);

        animalRepository.salvar(animal);
        return"redirect:/";
    }

    @GetMapping("/animal/listar")
    public String listar(Model model){
        List<Animal> animais = animalRepository.listarTodos();
        model.addAttribute("animais", animais);
        return"animal/listar";
    }

    @GetMapping("/animal/visualizar/{id}")
    public String visualizar(@PathVariable String id, Model model){
        List<Profissional> profissionais = profissionalRepository.listarTodos();
        Animal animal = animalRepository.obterId(id);

        model.addAttribute("profissionais", profissionais);
        model.addAttribute("animal", animal);
        return "animal/visualizar";
    }

    @GetMapping("/animal/excluir/{id}")
    public String excluir(@PathVariable String id){
        animalRepository.excluir(id);
        return "redirect:/animal/listar";
    }

    @GetMapping("/animal/relatorio/{format}")
    public String gerarRelatorio(@PathVariable String format) throws FileNotFoundException, JRException {
        report.exportaRelatorio(format);
        return "redirect:/";
    }
}
