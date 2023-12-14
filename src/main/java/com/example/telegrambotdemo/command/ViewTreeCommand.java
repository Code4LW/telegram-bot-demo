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
    private List<Element> sortList(List<Element> elements) {
        List<Element> sorted = new LinkedList<>();
        List<Element> childElements;
        Map<Element, Integer> map = new HashMap<>();
        for (int i = 0; i < elements.size(); i++) {
            if (!map.containsKey(elements.get(i))) {
                sorted.add(elements.get(i));
                map.put(elements.get(i), i);
            }
            final Element current = elements.get(i);
            childElements = elements.stream().filter(Element::hasParent).filter(element -> element.getParent().equals(current.getName())).collect(Collectors.toList());
            if(childElements.isEmpty()) continue;
            int index = map.get(elements.get(i));
            Element next = null;
            for (Element key:map.keySet()) {
                if (map.get(key)==index+1) {
                    next = key;
                    break;
                }
            }
            for (Element child : childElements) {
                index++;
                sorted.add(index, child);
                map.put(child,index);
            }
            if (next!=null) map.put(next,index+1);
        }
        return sorted;
    }
}
