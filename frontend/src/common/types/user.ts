import { TRoles } from './roles'

export type TUser = {
  _id: string
  name: string
  email: string
  role: TRoles
}
