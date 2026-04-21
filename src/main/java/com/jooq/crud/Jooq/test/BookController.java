package com.jooq.crud.Jooq.test;

import com.example.jooq.generated.tables.Book;
import org.jooq.DSLContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookInput bookInput) {
        ctx.insertInto(Book.BOOK)
                .set(Book.BOOK.AUTHOR_ID, bookInput.authorId)
                .set(Book.BOOK.TITLE, bookInput.title)
                .set(Book.BOOK.LANGUAGE_ID, bookInput.languageId)
                .set(Book.BOOK.PUBLISHED_IN, bookInput.publishedIn)
                .execute();
        return ResponseEntity.ok().build();
    }

    public record BookInput(
            String title,
            int authorId,
            int languageId,
            int publishedIn
    ) {
    }

    public record AuthorBookOutput(
            String firstName,
            String lastName,
            String title
    ) {
    }
}
