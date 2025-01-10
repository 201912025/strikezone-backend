package com.strikezone.strikezone_backend.domain.poll.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "poll_options")
public class PollOption {

    public PollOption(Poll poll, String optionText) {
        this.poll = poll;
        this.optionText = optionText;
    }

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

}
