package com.hanzul_v2.controller;

import com.hanzul_v2.domain.book.BookDto;
import com.hanzul_v2.domain.book.ReviewDto;
import com.hanzul_v2.domain.common.BasicResponse;
import com.hanzul_v2.domain.library.LibraryDto;
import com.hanzul_v2.repository.ReviewRepository;
import com.hanzul_v2.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "4.BookDetail")
@RestController
@RequestMapping("/")
public class BookController {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BookService bookService;

    @ApiOperation(value = "덤프 책 요청 20개")
    @GetMapping(value = "admin/dump")
    public ResponseEntity<List<BookDto>> reqDump()throws Exception{
        List<BookDto> respBookDto=bookService.giveDump();
        return ResponseEntity.ok().body(respBookDto);
    }

    @ApiOperation(value = "책 상세 정보 요청")
    @GetMapping(value = "detail/{book_isbn}")
    public ResponseEntity<BookDto> getBookDetail(
            @ApiParam(value = "book isbn")
            @PathVariable(name = "book_isbn") String bookIsbn
    )throws Exception{
        BookDto respBookDto=bookService.getBookDetail(bookIsbn);
        return ResponseEntity.ok().body(respBookDto);
    }

    @ApiOperation(value = "책 리뷰 정보 요청")
    @GetMapping(value = "detail/reivew/{book_isbn}")
    public ResponseEntity<List<ReviewDto.RespBookReviewDto>> getBookReview(
            @ApiParam(value = "book isbn")
            @PathVariable(name = "book_isbn") String bookIsbn
    )throws Exception{
        List<ReviewDto.RespBookReviewDto> respBookReviewDtoList=bookService.getBookReviews(bookIsbn);
        return ResponseEntity.ok().body(respBookReviewDtoList);
    }

    @ApiOperation(value = "책 스크랩 요청")
    @PostMapping(value = "detail/scrap")
    public ResponseEntity<?extends BasicResponse> setScrap(
            @ApiParam(value = "book info")
            @RequestBody LibraryDto.ReqLibrary reqLibrary
            )throws Exception{
        bookService.setScrap(reqLibrary);
        return ResponseEntity.ok().body(new BasicResponse(200,"suc"));
    }

    @ApiOperation(value = "책 스크랩 여부")
    @GetMapping(value = "detail/scrap/{book_isbn}/{user_id}")
    public ResponseEntity<Boolean> isScraped(
//            @ApiParam(value = "book_i)
            @PathVariable(name = "book_isbn") String isbn,
            @PathVariable(name = "user_id") String userId
    )throws Exception{
        Boolean aBoolean = bookService.isScraped(isbn,userId);
        return ResponseEntity.ok().body(aBoolean);
    }

    @ApiOperation(value = "책 스크랩 취소")
    @DeleteMapping(value = "detail/scrap/{book_isbn}/{user_id}")
    public ResponseEntity cancelScrap(
            @ApiParam(value = "book isbn")
            @PathVariable(name = "book_isbn") String isbn,
            @ApiParam(value = "user id")
            @PathVariable(name = "user_id") String userId
    )throws Exception{
        bookService.cancelScraped(isbn,userId);
        return new ResponseEntity<>(userId+"삭제완료", HttpStatus.OK);
    }

    @ApiOperation(value = "책 리뷰 입력 요청")
    @PostMapping(value = "detail/review")
    public ResponseEntity<Boolean> setReview(
            @ApiParam(value = "book review")
            @RequestBody ReviewDto.ReqBookDto reqBookDto
    )throws Exception{
        Boolean aBoolean = bookService.setReview(reqBookDto);
        return ResponseEntity.ok().body(aBoolean);
    }

    @ApiOperation(value = "책 별 해당 아이디 리뷰 전체 삭제 요청")
    @DeleteMapping(value = "admin/detail/review/{isbn}/{userId}")
    public ResponseEntity<Integer> deleteOneReview(
            @ApiParam(value = "book isbn")
            @PathVariable(name = "isbn") String isbn,
            @ApiParam(value = "user id")
            @PathVariable(name = "userId") String userId
    )throws Exception{
        int removedCnt= bookService.removeReview(isbn, userId);
        return ResponseEntity.ok().body(removedCnt);
    }
    @ApiOperation(value = "책 별 리뷰 전체 삭제 요청")
    @DeleteMapping(value = "admin/detail/review/{isbn}")
    public ResponseEntity<Integer> deletAllReview(
            @ApiParam(value = "book isbn")
            @PathVariable(name = "isbn") String isbn
    )throws Exception{
        int removedCnt = bookService.removeAllReview(isbn);
        return ResponseEntity.ok().body(removedCnt);
    }
}
