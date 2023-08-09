import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { TStatus } from '../types/status.type';
import { TGroup } from '../types/group.type';
import mongoose, { Document } from 'mongoose';

@Schema({
  timestamps: true,
})
export class Conversation extends Document {
  @Prop({ type: Object })
  members: TGroup;

  @Prop()
  status: TStatus;

  @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'Message' })
  lastMessage: string;
}

export const ConversationSchema = SchemaFactory.createForClass(Conversation);
