package com.github.mtahasahin.evently.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "FOLLOWER_FOLLOWING")
public class FollowerFollowing extends Auditable {

    @EmbeddedId
    private FollowerFollowingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followerId")
    private AppUser follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followingId")
    private AppUser following;

    @Column(name = "CONFIRMED")
    private boolean confirmed;

    public FollowerFollowing(AppUser follower, AppUser following, boolean confirmed) {
        this.follower = follower;
        this.following = following;
        this.confirmed = confirmed;
        this.id = new FollowerFollowingId(follower.getId(), following.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        FollowerFollowing that = (FollowerFollowing) o;
        return Objects.equals(follower, that.follower) &&
                Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }
}
