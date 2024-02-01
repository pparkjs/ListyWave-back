package com.listywave.list.application.domain;

import com.listywave.list.application.vo.ListDescription;
import com.listywave.list.application.vo.ListTitle;
import com.listywave.user.application.domain.User;
import com.listywave.list.presentation.dto.request.ItemCreateRequest;
import com.listywave.list.application.vo.ItemComment;
import com.listywave.list.application.vo.ItemImageUrl;
import com.listywave.list.application.vo.ItemLink;
import com.listywave.list.application.vo.ItemTitle;
import com.listywave.list.application.vo.LabelName;
import com.listywave.list.application.dto.ListCreateCommand;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "LIST")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Convert(converter = CategoryTypeConverter.class)
    private CategoryType category;

    @Builder.Default
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Label> labels = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    @Embedded
    private ListTitle title;

    @Embedded
    private ListDescription description;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private String backgroundColor;

    @Column(nullable = false)
    private boolean hasCollaboration;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int collectCount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public static void addLabelToList(Lists list, List<String> labels) {
        for (String label : labels) {
            list.getLabels().add(
                    Label.builder()
                            .list(list)
                            .labelName(
                                    LabelName.builder()
                                            .value(label)
                                            .build()
                            )
                            .build()
            );
        }
    }

    private static void addItemToList(Lists list, List<ItemCreateRequest> items) {
        for (ItemCreateRequest item : items) {
            list.getItems().add(
                    Item.builder()
                            .list(list)
                            .ranking(item.rank())
                            .title(
                                    ItemTitle.builder()
                                            .value(item.title())
                                            .build()
                            )
                            .comment(
                                    ItemComment.builder()
                                            .value(item.comment())
                                            .build()
                            )
                            .link(
                                    ItemLink.builder()
                                            .value(item.link())
                                            .build()
                            )
                            .imageUrl(
                                    ItemImageUrl.builder()
                                            .value(item.imageUrl())
                                            .build()
                            )
                            .build()
            );
        }
    }
    public static Lists createList(
            User user,
            ListCreateCommand list,
            List<String> labels,
            List<ItemCreateRequest> items,
            Boolean isLabels,
            Boolean hasCollaboratorId){
        Lists lists = Lists.builder()
                .user(user)
                .category(list.category())
                .hasCollaboration(hasCollaboratorId)
                .title(list.title())
                .isPublic(list.isPublic())
                .backgroundColor(list.backgroundColor())
                .description(list.description())
                .build();

        if(isLabels){
            addLabelToList(lists, labels);
        }
        addItemToList(lists, items);
        return lists;
    }
}