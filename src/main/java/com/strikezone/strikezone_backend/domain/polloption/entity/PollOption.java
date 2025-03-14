package com.strikezone.strikezone_backend.domain.polloption.entity;

import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PollOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(nullable = false, length = 200)
    private String optionText;

    @Column(nullable = false)
    private Integer votesCount = 0;

    @Builder
    public PollOption(Poll poll, String optionText) {
        this.poll = poll;
        this.optionText = optionText;
    }

    public void incrementVotesCount() {
        this.votesCount += 1;
    }

    public void decrementVotesCount() {
        if (this.votesCount > 0) {
            this.votesCount -= 1;
        }
    }

}
