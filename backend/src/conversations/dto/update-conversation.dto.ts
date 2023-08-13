import { PartialType } from '@nestjs/mapped-types';
import { Conversation } from '../entities/conversation.entity';
import { TStatus } from '../types/status.type';
import { IsNotEmpty, IsString } from 'class-validator';
import { TGroup } from '../types/group.type';

export class UpdateConversationDto extends PartialType(Conversation) {
  @IsNotEmpty()
  @IsString()
  id: string;

  members: TGroup;

  @IsNotEmpty()
  status: TStatus;
}
