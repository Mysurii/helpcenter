import { TUser } from './user'

export type TMessage = {
  text: string
  sender: TUser
  conversationId: string
  createdAt: Date
}
