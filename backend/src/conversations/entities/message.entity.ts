import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import mongoose, { Document } from 'mongoose';

@Schema({
  timestamps: true,
})
export class Message extends Document {
  @Prop()
  message: string;

  @Prop()
  sender: string;

  @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'Conversation' })
  conversationId: string;
}

export const MessageSchema = SchemaFactory.createForClass(Message);
