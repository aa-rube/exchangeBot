package app.fiveminchange;

import app.bot.config.BotConfig;
import app.fiveminchange.model.RequestDetails;
import app.ipinfo.IpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateMsg {
    @Autowired
    private IpInfoService ipInfo;
    @Autowired
    private BotConfig config;
    private final String underLine = "\n___________________________________\n\n";

    public SendMessage getNewSendMessage(RequestDetails request) {
        String text = createMsgText(request);
        System.out.println(text);
        List<MessageEntity> entities = createEntities(request, text);

        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(config.getAdminChatId());
        sendMsg.setText(text);
        sendMsg.setEntities(entities);
        return sendMsg;
    }


    private String createMsgText(RequestDetails request) {
        return new StringBuilder()
                .append(ipInfo.getEmoji(request.getIpAddress().trim())).append("Новая заявка\n\n")
                .append("ID Заявки\n").append(request.getRequestId()).append("\n")
                .append(underLine)
                .append("Получаем\n").append(request.getFromBank().getCode()).append(" ")
                .append(request.getFromValue()).append("\n").append(request.getFromAccount()).append("\n")
                .append("\nОтдаем\n").append(request.getToBank().getCode()).append(" ")
                .append(request.getToValue()).append("\n")
                .append(request.getToAccount())
                .append(underLine)
                .append("\uD83D\uDCC7 Комиссия: ").append(request.getCommission()).append("\n\n")
                .append("\uD83D\uDCB8 Прибыль\n").append(getProfit(request))
                .append(underLine)
                .append("\uD83D\uDCEC Контактные данные\n").append(request.getUser().getEmail()).append("\n\n")
                .append("\uD83C\uDFDD IP адрес\n").append(request.getIpAddress()).append(underLine)
                .append("\uD83D\uDCAD Комментарий:\n").append(request.getStatusMessage() == null ? "Комментария нет" : request.getStatusMessage())
                .toString();
    }

    private String getProfit(RequestDetails request) {
        double commissionMultiplier = (request.getRateIn() == 1) ? 100 - request.getCommission() : 100 + request.getCommission();
        double profit = (request.getToValue() / commissionMultiplier) * request.getCommission();

        return profit + " " + request.getFromBank().getCode() + "\n";
    }


    private List<MessageEntity> createEntities(RequestDetails request, String messageText) {
        List<MessageEntity> entities = new ArrayList<>();

        addMessageEntity(entities, request.getFromAccount(), messageText);
        addMessageEntity(entities, request.getToAccount(), messageText);
        addMessageEntity(entities, String.valueOf(request.getFromValue()), messageText);
        addMessageEntity(entities, String.valueOf(request.getToValue()), messageText);

        return entities;
    }

    private void addMessageEntity(List<MessageEntity> entities, String fragment, String messageText) {
        if (fragment != null) {
            MessageEntity entity = new MessageEntity();
            entity.setType("code");
            entity.setText(fragment);
            entity.setLength(fragment.length());
            entity.setOffset(messageText.indexOf(fragment));
            entities.add(entity);
        }
    }
}
