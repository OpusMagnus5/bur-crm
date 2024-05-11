package pl.bodzioch.damian.program;

import java.util.Optional;

public record InnerProgramDto(
        Long id,
        String name
) {
        public InnerProgramDto(InnerProgram program) {
                this(
                        Optional.ofNullable(program).map(InnerProgram::id).orElse(null),
                        Optional.ofNullable(program).map(InnerProgram::name).orElse(null)
                );
        }
}
