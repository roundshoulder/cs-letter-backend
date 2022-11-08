package com.project.csletter.member.repository;

import com.project.csletter.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member findByKakaoEmail(String kakaoEmail);

    public Member findByUserCode(String userCode);

}
