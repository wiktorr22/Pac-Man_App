package com.example.server.GameResult;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class GameResult {

    @Id
    @SequenceGenerator(
            name = "result_sequence",
            sequenceName = "result_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "result_sequence"
    )
    private Long id;
    private LocalDate date;
    private int result;
    private int livesLeft;

    @Transient
    private boolean win;

    public GameResult() {

    }

    public GameResult(LocalDate date, int result, int livesLeft) {
        this.date = date;
        this.result = result;
        this.livesLeft = livesLeft;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getLivesLeft() {
        return livesLeft;
    }

    public void setLivesLeft(int livesLeft) {
        this.livesLeft = livesLeft;
    }

    public boolean isWin() {
        return getLivesLeft() > 0;
    }

    public void setWin(boolean won) {
        this.win = won;
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "id=" + id +
                ", date=" + date +
                ", result=" + result +
                ", livesLeft=" + livesLeft +
                ", isWin=" + isWin() +
                '}';
    }
}
