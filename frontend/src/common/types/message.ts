import { TUser } from './user'

export type TMessage = {
  text: string
  sender: TUser
  conversation: string
  createdAt: Date
}
