package com.jooq.crud.Jooq.test;

import com.example.jooq.generated.tables.Book;
import org.jooq.DSLContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.example.jooq.generated.Tables.AUTHOR;
import static com.example.jooq.generated.Tables.BOOK;

@RestController
@RequestMapping("/v1/books")
public class BookController {

    private final DSLContext ctx;

    public BookController(DSLContext ctx) {
        this.ctx = ctx;
    }

    @GetMapping
    public ResponseEntity<?> getBooks() {
        return ResponseEntity.of(Optional.of(ctx.selectFrom(Book.BOOK).fetchInto(com.example.jooq.generated.tables.pojos.Book.class)));
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable Long id) {

        var result = ctx.select(AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME, BOOK.TITLE)
                .from(AUTHOR)
                .leftJoin(BOOK)
                .on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                .where(AUTHOR.ID.eq(id.intValue()));
//                .fetchInto(AuthorBook.class);

        return ResponseEntity.ok(result.getSQL());
    }

    public record AuthorBook(String firstName, String lastName, String title) {

    }
}
