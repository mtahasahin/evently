package com.github.mtahasahin.evently.entity;

import lombok.*;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class AppUser implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_AUTHORITIES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID"))
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "organizer", orphanRemoval = true)
    private Set<Event> organizedEvents = new HashSet<>();

    public boolean isOrganizing(Event event) {
        return organizedEvents.contains(event);
    }

    @OneToMany(mappedBy = "applicant", orphanRemoval = true)
    private Set<EventApplication> eventApplications = new HashSet<>();

    public boolean isJoiningEvent(Event event) {
        return eventApplications.stream().anyMatch(e -> e.getEvent() == event && e.isConfirmed());
    }

    public boolean isWaitingForApprovalForEvent(Event event) {
        return eventApplications.stream().anyMatch(e -> e.getEvent() == event && !e.isConfirmed());
    }

    @OneToMany(
            mappedBy = "following",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FollowerFollowing> followers = new ArrayList<>();

    @OneToMany(
            mappedBy = "follower",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FollowerFollowing> followings = new ArrayList<>();

    public void addFollower(AppUser user, boolean confirmed) {
        FollowerFollowing followerFollowing = new FollowerFollowing(user, this, confirmed);
        followers.add(followerFollowing);
        user.followings.add(followerFollowing);
    }

    public void removeFollower(AppUser user) {
        for (Iterator<FollowerFollowing> iterator = followers.iterator(); iterator.hasNext(); ) {
            FollowerFollowing followerFollowing = iterator.next();

            if (followerFollowing.getFollowing().equals(this) &&
                    followerFollowing.getFollower().equals(user)) {
                iterator.remove();
                followerFollowing.getFollower().getFollowings().remove(followerFollowing);
                followerFollowing.setFollower(null);
                followerFollowing.setFollowing(null);
            }
        }
    }

    public boolean isFollowing(AppUser user) {
        return followings.stream().anyMatch(e -> e.getFollowing() == user && e.isConfirmed());
    }

    public boolean hasFollowingRequest(AppUser user) {
        return followings.stream().anyMatch(e -> e.getFollowing() == user && !e.isConfirmed());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }
}
