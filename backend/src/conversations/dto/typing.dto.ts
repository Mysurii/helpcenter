import { IsBoolean, IsNotEmpty, IsString } from 'class-validator';

export class TypingDto {
  @IsString()
  @IsNotEmpty()
  conversationId: string;

  @IsNotEmpty()
  @IsBoolean()
  typing: boolean;
}
