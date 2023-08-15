package com.profiteer.core.api.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "contest_order_detail", indexes = { @Index(columnList = "id"), @Index(columnList = "contest_id") })
@Data
public class ContestOrderWinner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, precision = 10)
    private Long id;

    @Column(name = "winner_user_id")
    private Long winnerUserId;

    @Column(name = "winner_amount")
    private Double winnerAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "winning_rank")
    private WinningRank winningRank;

    @ManyToOne
    @JoinColumn(name="contestDetail_id", nullable=false)
    private ContestDetail contestDetail;



}
