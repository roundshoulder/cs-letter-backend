package com.project.csletter.member.repository;

import com.project.csletter.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByKakaoEmail(String kakaoEmail);

    Optional<Member> findByKakaoId(String kakaoId);

    Optional<Member> findByKakaoNickname(String kakaoNickname);

    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findByUserCode(Long userCode);



}
