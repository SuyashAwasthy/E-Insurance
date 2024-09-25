package com.techlabs.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "file_items")
public class FileItem {
   @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @NotBlank(message = "Name of File cannot be empty")
      private String name;

      @NotBlank(message = "File type must be defined")
      private String type;

      @NotBlank(message = "File location must be specified")
      private String location;

    // Constructors
   
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Builder class
//    public static class Builder {
//        private Long id;
//        private String name;
//        private String type;
//        private String location;
//
//        public Builder id(Long id) {
//            this.id = id;
//            return this;
//        }
//
//        public Builder name(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public Builder type(String type) {
//            this.type = type;
//            return this;
//        }
//
//        public Builder location(String location) {
//            this.location = location;
//            return this;
//        }
//
//        public FileItem build() {
//            return new FileItem(id, name, type, location);
//        }
//    }
//
//    // Static method to create a new builder
//    public static Builder builder() {
//        return new Builder();
//    }
}