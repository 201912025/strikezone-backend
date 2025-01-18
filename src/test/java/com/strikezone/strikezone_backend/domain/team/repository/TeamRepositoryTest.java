package com.strikezone.strikezone_backend.domain.team.repository;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.exception.TeamExceptionType;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @DisplayName("팀 이름으로 팀을 조회한다.")
    @Test
    void findByName() {
        // given
        Team team1 = Team.builder()
                .name(TeamName.KIA)
                .build();
        teamRepository.save(team1);

        // when
        Optional<Team> foundTeam = teamRepository.findByName(TeamName.KIA);

        // then
        assertThat(foundTeam).isPresent();
        assertThat(foundTeam.get().getName()).isEqualTo(TeamName.KIA);
    }

    @DisplayName("팀 이름으로 팀이 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void findByNameNotFound() {
        // when & then
        assertThatThrownBy(() -> {
            teamRepository.findByName(TeamName.LG)
                    .orElseThrow(() -> new NotFoundException(TeamExceptionType.NOT_FOUND_TEAM));
        }).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("팀을 찾을 수 없습니다.");
    }

    @DisplayName("중복된 팀 이름이 있을 때 true를 반환한다.")
    @Test
    void existsByName() {
        // given
        Team team1 = Team.builder()
                .name(TeamName.KIA)
                .build();
        teamRepository.save(team1);

        // when
        boolean exists = teamRepository.existsByName(TeamName.KIA);

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("중복된 팀 이름이 없을 때 false를 반환한다.")
    @Test
    void existsByNameFalse() {
        // when
        boolean exists = teamRepository.existsByName(TeamName.LG);

        // then
        assertThat(exists).isFalse();
    }
}
