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
        System.out.println(text);//just for test
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
                .append(formatValue(request.getFromValue())).append("\n")
                .append(request.getFromAccount()).append("\n")

                .append("\nОтдаем\n").append(request.getToBank().getCode()).append(" ")
                .append(formatValue(request.getToValue())).append("\n")
                .append(request.getToAccount())
                .append(underLine)

                .append("\uD83D\uDCC7 Комиссия: ").append(request.getCommission()).append("\n\n")
                .append("\uD83D\uDCB8 Прибыль\n").append(getProfit(request))
                .append(underLine)

                .append("\uD83D\uDCEC Контактные данные\n").append(request.getUser() == null ? "Почта не была указана" : request.getUser().getEmail()).append("\n\n")
                .append("\uD83C\uDFDD IP адрес\n").append(request.getIpAddress())
                .append(underLine)

                .append("\uD83D\uDCAD Комментарий:\n").append(request.getStatusMessage() == null ? "Комментария нет" : request.getStatusMessage())
                .toString();
    }

    private static String formatValue(double value) {
        NumberFormat numberFormat = DecimalFormat.getInstance();
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(19);
        return numberFormat.format(value);
    }

    private String getProfit(RequestDetails request) {
        double commissionMultiplier = (request.getRateIn() == 1) ? 100 - request.getCommission() : 100 + request.getCommission();
        double divisible = (request.getRateIn() == 1 ? request.getToValue() : request.getFromValue());
        String str = formatValue((divisible / commissionMultiplier) * request.getCommission());

        return str.substring(0, Math.min(str.length(), 14)) + " " + request.getFromBank().getCode() + "\n";
    }

    private List<MessageEntity> createEntities(RequestDetails request, String messageText) {
        List<MessageEntity> entities = new ArrayList<>();

        addMessageEntityCode(entities, request.getFromAccount(), messageText);
        addMessageEntityCode(entities, formatValue(request.getFromValue()), messageText);
        addMessageEntityCode(entities, formatValue(request.getToValue()), messageText);
        addMessageEntityCode(entities, request.getToAccount(), messageText);

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