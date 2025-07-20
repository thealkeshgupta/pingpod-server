package com.thealkeshgupta.PingPod.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    @EqualsAndHashCode.Include
    private Long roomId;

    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @NotBlank
    private String createdAt;

    @Getter
    @Setter
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> members = new HashSet<>();
}
