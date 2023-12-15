package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.model.Element;
import com.example.telegrambotdemo.receiver.BotMessageService;
import com.example.telegrambotdemo.receiver.ElementService;
import com.fasterxml.jackson.databind.type.TypeParser;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class ViewTreeCommand implements BotCommand{

    private final BotMessageService botMessageService;


    @Override
    public void execute(Update update) {
        if(botMessageService.getElementService().getAll().isEmpty()||botMessageService.getElementService().getAll()==null){
            botMessageService.sendMessage(update.getMessage().getChatId(), "The tree is empty, you can create it by adding a new element");
        }else {
            botMessageService.sendMessage(update.getMessage().getChatId(), drawTree());
        }
    }
    //Реализация метода для построения схемы дерева из отсортированного списка
    private String drawTree(){
        List<Element> elements = botMessageService.getElementService().getAll();
        StringBuilder stringBuilder = new StringBuilder();
        elements = sortList(elements);
        String category;
        int lvl ;
        for(Element element:elements){
            lvl = element.getLevel()-1;
            category = element.getName();
            if(element.getLevel()>1) {
                category = " └── "+category;
            }
            for (int i = 1; i < lvl*2; i++) {
                category = "    \t" + category;
            }
            stringBuilder.append(category+": \n");
        }
        return stringBuilder.toString();
    }

    //Реализация сортировки списка элементов для построения дерева
    private List<Element> sortList(List<Element> elements) {
        //Создаем LinkedList объект так как туда будет удобнее добавлять новые элементы в список
        List<Element> sorted = new LinkedList<>();
        List<Element> childElements;
        Set<Element> set = new HashSet<>();
        for (int i = 0; i < elements.size(); i++) {
            if (set.add(elements.get(i))) {
                sorted.add(elements.get(i));
            }
            final Element current = elements.get(i);
            // ищем все дочерние элементы данного элемента

            childElements = elements.stream().filter(Element::hasParent).filter(element -> element.getParent().equals(current.getName())).collect(Collectors.toList());
            if(childElements.isEmpty()) continue;

            //При наличии дочерних элементов добавляем кажыдй из них в отсортированный список после родительского элемента
            int index = sorted.indexOf(elements.get(i));

            for (Element child : childElements) {
                index++;
                sorted.add(index, child);
                set.add(child);
            }

        }

        return sorted;
    }
}
