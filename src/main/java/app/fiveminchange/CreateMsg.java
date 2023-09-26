package app.fiveminchange;

import app.bot.config.BotConfig;
import app.fiveminchange.model.RequestDetails;
import app.ipinfo.IpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateMsg {
    @Autowired
    private IpInfoService ipInfo;
    @Autowired
    private BotConfig config;
    private final String underLine = "\n___________________________________\n\n";

    public SendMessage getNewSendMessage(RequestDetails request, String filter) {
        String text = createMsgText(request, filter);
        System.out.println(text);
        List<MessageEntity> entities = createEntities(request, text);

        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(config.getAdminChatId());
        sendMsg.setText(text);
        sendMsg.setEntities(entities);
        return sendMsg;
    }

    private String createMsgText(RequestDetails request, String filter) {
        return new StringBuilder()
                .append(ipInfo.getEmoji(request.getIpAddress().trim())).append("Новая заявка\n\n")
                .append("ID Заявки\n").append(request.getRequestId()).append(filter.equals("verify") ? " (не верифицировано)" : "").append("\n")
                .append(underLine)
                .append("Получаем\n").append(request.getFromBank().getCode()).append(" ")
                .append(formatNumber(request.getFromValue())).append("\n").append(request.getFromAccount()).append("\n")
                .append("\nОтдаем\n").append(request.getToBank().getCode()).append(" ")
                .append(formatNumber(request.getToValue())).append("\n")
                .append(request.getToAccount())
                .append(underLine)
                .append("\uD83D\uDCC7 Комиссия: ").append(request.getCommission()).append("\n\n")
                .append("\uD83D\uDCB8 Прибыль\n").append(getProfit(request))
                .append("\uD83D\uDCEC Контактные данные\n").append(getEmail(request)).append("\n\n")
                .append("\uD83C\uDFDD IP адрес\n").append(request.getIpAddress()).append(underLine)
                .append("\uD83D\uDCAD Комментарий:\n").append(request.getStatusMessage() == null ? "Комментария нет" : request.getStatusMessage())
                .toString();
    }

    private static String formatNumber(double number) {
        NumberFormat numberFormat = DecimalFormat.getInstance();
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(15);
        return numberFormat.format(number).trim();
    }

    private String getEmail(RequestDetails request) {
        try {
            return request.getUser().getEmail();
        } catch (Exception e) {
        }
        return "Почта не была указана";
    }

    private String getProfit(RequestDetails request) {
        double commissionMultiplier = (request.getRateIn() == 1) ? 100 - request.getCommission() : 100 + request.getCommission();
        String str = formatNumber(((request.getRateIn() == 1 ? request.getToValue() : request.getFromValue()) / commissionMultiplier) * request.getCommission());

        return str + " " + request.getFromBank().getCode() + "\n" + underLine;
    }


    private List<MessageEntity> createEntities(RequestDetails request, String messageText) {
        List<MessageEntity> entities = new ArrayList<>();

        addMessageEntityCode(entities, request.getFromAccount(), messageText);
        addMessageEntityCode(entities, request.getToAccount(), messageText);
        addMessageEntityCode(entities, formatNumber(request.getFromValue()), messageText);
        addMessageEntityCode(entities, formatNumber(request.getToValue()), messageText);

        if(messageText.contains("(не верифицировано)")) {
            addMessageEntityBold(entities, "(не верифицировано)", messageText);
        }

        return entities;
    }

    private void addMessageEntityCode(List<MessageEntity> entities, String fragment, String messageText) {
        if (fragment != null) {
            MessageEntity entity = new MessageEntity();
            entity.setType("code");
            entity.setText(fragment);
            entity.setLength(fragment.length());
            entity.setOffset(messageText.indexOf(fragment));
            entities.add(entity);
        }
    }

    private void addMessageEntityBold(List<MessageEntity> entities, String fragment, String messageText) {
        if (fragment != null) {
            MessageEntity entity = new MessageEntity();
            entity.setType("bold");
            entity.setText(fragment);
            entity.setLength(fragment.length());
            entity.setOffset(messageText.indexOf(fragment));
            entities.add(entity);
        }
    }
}