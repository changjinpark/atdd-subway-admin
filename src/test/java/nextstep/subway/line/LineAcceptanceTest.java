package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 왕십리역;
    private StationResponse 수원역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        수원역 = 지하철역_등록되어_있음("수원역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("분당선", "bg-yellow-200", 왕십리역.getId(), 수원역.getId(), 20);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(Arrays.asList(createResponse1, createResponse2), response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineRequest2, createResponse1);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse) {
        String uri = getURI(createResponse);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse lineResponse) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineRequest lineRequest2, ExtractableResponse<Response> createResponse) {
        String uri = getURI(createResponse);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(lineRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(uri)
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> createResponse) {
        String uri = getURI(createResponse);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all().extract();
        return response;
    }

    public static String getURI(ExtractableResponse<Response> createResponse) {
        return createResponse.header("Location");
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_목록_포함됨(List<ExtractableResponse<Response>> createResponses, ExtractableResponse<Response> response) {
        List<Long> expectedLineIds = createResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_수정되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
