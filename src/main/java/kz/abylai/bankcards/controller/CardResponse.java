package kz.abylai.bankcards.controller;

import kz.abylai.bankcards.dto.CardDTO;

import java.util.List;

public class CardResponse {
    private List<CardDTO> cardDTOList;

    public CardResponse(List<CardDTO> cardDTOList) {
        this.cardDTOList = cardDTOList;
    }

    public List<CardDTO> getCardDTOList() {
        return cardDTOList;
    }

    public void setCardDTOList(List<CardDTO> cardDTOList) {
        this.cardDTOList = cardDTOList;
    }
}
