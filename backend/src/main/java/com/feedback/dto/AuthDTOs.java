@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public static class AuthResponse {
    private String token;

    @Builder.Default
    private String type = "Bearer";  // ✅ won't be null when using builder()

    private Long id;
    private String username;
    private String email;
    private String role;
}