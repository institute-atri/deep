package org.instituteatri.deep.repositories;

import org.instituteatri.deep.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

  @Query("SELECT t FROM Token t JOIN t.user u WHERE u.id = :id AND (t.tokenExpired = false OR t.tokenRevoked = false)")
  List<Token> findAllValidTokenByUser(String id);
  List<Token> findAllByUserId(String userId);
  Optional<Token> findByTokenValue(String token);
}
