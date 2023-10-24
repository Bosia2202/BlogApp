package com.denisvasilenko.BlogApp.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "subscriptions")
public class Subscription {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        long id;
        @Column(name = "subscription")
        String subscriptionUserName;
        @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
        @JoinColumn(name = "users_id",referencedColumnName = "id")
        private User userOwner;

        public Subscription(String subscriptionUserName) {
                this.subscriptionUserName = subscriptionUserName;
        }
        public Subscription(){}

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public String getSubscriptionUserName() {
                return subscriptionUserName;
        }

        public void setSubscriptionUserName(String subscriptionUserName) {
                this.subscriptionUserName = subscriptionUserName;
        }

        public User getUserOwner() {
                return userOwner;
        }

        public void setUserOwner(User userOwner) {
                this.userOwner = userOwner;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Subscription that = (Subscription) o;
                return id == that.id && Objects.equals(subscriptionUserName, that.subscriptionUserName) && Objects.equals(userOwner, that.userOwner);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, subscriptionUserName, userOwner);
        }
}
