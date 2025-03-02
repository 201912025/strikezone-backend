package com.strikezone.strikezone_backend.domain.vote.entity;

import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import com.strikezone.strikezone_backend.domain.polloption.PollOption;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private PollOption option;

    private LocalDateTime votedAt = LocalDateTime.now();

    @Builder
    public Vote(User user, Poll poll, PollOption option) {
        this.user = user;
        this.poll = poll;
        this.option = option;
    }
}
