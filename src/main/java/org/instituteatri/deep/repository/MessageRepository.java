package org.instituteatri.deep.repository;

import org.instituteatri.deep.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
}
