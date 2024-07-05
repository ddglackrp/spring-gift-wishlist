package gift.controller;

import gift.domain.AuthToken;
import gift.dto.request.WishCreateRequest;
import gift.dto.request.WishEditRequest;
import gift.dto.response.WishResponseDto;
import gift.service.WishService;
import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/wishes")
public class WishController {

    private final TokenService tokenService;
    private final WishService wishService;

    public WishController(TokenService tokenService, WishService wishService) {
        this.tokenService = tokenService;
        this.wishService = wishService;
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishProducts(HttpServletRequest request){
        AuthToken token = getAuthVO(request);

        List<WishResponseDto> findProducts = wishService.findAllLikesProducts(token.email());

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(findProducts);
    }

    @PostMapping
    public String addWishProduct(HttpServletRequest request,
                                 @RequestBody WishCreateRequest wishCreateRequest){
        AuthToken token = getAuthVO(request);

        wishService.addLikesProduct(wishCreateRequest.product_id(), token.email(), wishCreateRequest.count());

        return "redirect:/wishes";
    }


    @PutMapping
    public String editWishProduct(HttpServletRequest request,
                                  @RequestBody WishEditRequest wishEditRequest){
        AuthToken token = getAuthVO(request);
        wishService.editLikes(wishEditRequest.wish_id(), token.email() , wishEditRequest.count());
        return "redirect:/wishes";
    }

    @DeleteMapping
    public String deleteLikesProduct(HttpServletRequest request,
                                     @RequestBody WishEditRequest wishEditRequest){
        AuthToken token = getAuthVO(request);
        wishService.deleteLikes(wishEditRequest.wish_id(), token.email());
        return "redirect:/wishes";
    }

    public AuthToken getAuthVO(HttpServletRequest request) {
        String key = request.getHeader("Authorization").substring(7);
        AuthToken token = tokenService.findToken(key);
        return token;
    }

}