package app.fiveminchange;

import app.fiveminchange.model.RequestDetails;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateMsg {
    private final String underLine = "\n____________________\n";
    private final String eng = "\uD83C\uDDEC\uD83C\uDDE7";
    private final String rus = "\uD83C\uDDF7\uD83C\uDDFA";

    public SendMessage getNewSendMessage(RequestDetails request) {
        String text = createMsgText(request);
        List<MessageEntity> entities = createEntities(request, text);

        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(795363892L);
        sendMsg.setText(text);
        sendMsg.setEntities(entities);
        return sendMsg;
    }

    private String createMsgText(RequestDetails request) {
        StringBuffer builder = new StringBuffer();

        builder.append(rus).append(eng).append("Новая заявка\n");

        builder.append("ID Заявки\n").append(request.getRequestId()).append(underLine);

        builder.append("\nПолучаем\n").append(request.getFromBank().getCode()).append(" ")
                .append(request.getFromValue()).append("\n").append(request.getFromAccount()).append("\n");

        builder.append("\nОтдаем\n").append(request.getToBank().getCode()).append(" ")
                .append(request.getToValue()).append("\n")
                .append(request.getToAccount()).append(underLine);

        builder.append("\uD83D\uDCC7 Комиссия: ").append(request.getCommission()).append("\n");
        builder.append("\uD83D\uDCB8 Прибыль\n").append("сумма прибыли").append(" ")
                .append("SBERRUB\n").append(underLine);

        builder.append("\uD83D\uDCEC Контактные данные\n").append("email does not provide from json\n");

        builder.append("\uD83C\uDFDD IP адрес\n").append(request.getIpAddress()).append(underLine);

        builder.append("\uD83D\uDCAD Комментарий:\n").append(request.getStatusMessage());

        return builder.toString();
    }


    private List<MessageEntity> createEntities(RequestDetails request, String messageText) {
        List<MessageEntity> entities = new ArrayList<>();

        MessageEntity first = new MessageEntity();
        String firstFragment = request.getFromAccount();
        if (firstFragment != null) {
            first.setType("code");
            first.setText(firstFragment);
            first.setLength(firstFragment.length());
            first.setOffset(messageText.indexOf(firstFragment));
            entities.add(first);
        }

        MessageEntity second = new MessageEntity();
        String secondFragment = request.getToAccount();

        if(secondFragment != null) {
            second.setType("code");
            second.setText(secondFragment);
            second.setLength(secondFragment.length());
            second.setOffset(messageText.indexOf(secondFragment));
            entities.add(second);
        }
        return entities;
    }
}
