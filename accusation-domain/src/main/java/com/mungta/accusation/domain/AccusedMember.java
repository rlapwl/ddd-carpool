package com.mungta.accusation.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AccusedMember {

    @Column(name = "ACCUSED_MEMBER_ID", nullable = false)
    private String id;

    @Column(name = "ACCUSED_MEMBER_NAME", nullable = false)
    private String name;

    @Column(name = "ACCUSED_MEMBER_EMAIL", nullable = false)
    private String emailAddress;

    @Builder
    public AccusedMember(String id, String name, String emailAddress) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccusedMember that = (AccusedMember) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, emailAddress);
    }

}
