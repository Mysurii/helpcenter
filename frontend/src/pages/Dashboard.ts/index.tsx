import withNavbarProvided from "@/common/components/layout/Navbar"
import { Button } from "@/common/components/ui/button"
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "@/common/components/ui/table"
import { TUser } from "@/common/types/user"
import UserService from "@/setup/services/UserService"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/common/components/ui/dialog";
import { useEffect, useState } from "react"
import { MdDeleteForever } from 'react-icons/md'
import { Input } from "@/common/components/ui/input"
import { useForm } from "react-hook-form"
import { TRegisterSchema, registerSchema } from "@/common/validations/signup.validation"
import { zodResolver } from "@hookform/resolvers/zod"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/common/components/ui/form"
import { useMutation } from "react-query"
import toast from "react-hot-toast"

function Dashboard () {
  const [ users, setUsers ] = useState<TUser[]>( [] )

  const form = useForm<TRegisterSchema>( {
    resolver: zodResolver( registerSchema ),
    defaultValues: {
      name: "",
      email: "",
      password: ""
    },
  } )

  const mutation = useMutation( {
    mutationFn: UserService.signup,
    onSuccess: ( data ) => {
      toast.success( 'Successfully registered account.' )
    },
    onError: ( err ) => {
      toast.error( 'Error while trying to create your account.' )
    }

  } )

  const deleteUserMutation = useMutation( {
    mutationFn: UserService.deleteUser,
    onSuccess: ( data ) => {
      toast.success( 'Successfully deleted the user.' )
    },
    onError: ( err ) => {
      toast.error( 'Failed to delete the user.' )
    }

  } )

  const onSubmit = ( values: TRegisterSchema ) => {
    mutation.mutate( values )
  }

  const deleteUser = ( id: string ) => {
    deleteUserMutation.mutate( id )
  }



  useEffect( () => {
    const fetchUsers = async () => {
      const data = await UserService.getPortalUsers()
      setUsers( data )
    }
    fetchUsers()

  } )

  return (
    <div className="container mt-5 text-right">
      <Dialog >
        <DialogTrigger asChild >
          <Button variant='destructive' className="text-primary text-right">Add user +</Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Add Portal account</DialogTitle>
            <DialogDescription>
              Add an admin user (can make use of this portal) </DialogDescription>
          </DialogHeader>
          <Form {...form}>
            <form onSubmit={form.handleSubmit( onSubmit )} className=" w-full max-w-lg">
              <Form {...form}>
                <form onSubmit={form.handleSubmit( onSubmit )} className=" w-full max-w-lg">
                  <FormField
                    control={form.control}
                    name="name"
                    render={( { field } ) => (
                      <FormItem className="mt-4">
                        <FormLabel>Name</FormLabel>
                        <FormControl>
                          <Input placeholder="name" {...field} />
                        </FormControl>

                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="email"
                    render={( { field } ) => (
                      <FormItem className="mt-4">
                        <FormLabel>E-mail</FormLabel>
                        <FormControl>
                          <Input placeholder="email" {...field} />
                        </FormControl>

                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="password"
                    render={( { field } ) => (
                      <FormItem className="mt-4">
                        <FormLabel>Password</FormLabel>
                        <FormControl>
                          <Input placeholder="password" type="password"  {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <Button variant="outline" type="submit" size="sm" className="text-violet-700 rounded-3xl px-8 mt-12">Add account</Button>
                </form>
              </Form>
            </form>
          </Form>
        </DialogContent>
      </Dialog>
      <Table>
        <TableCaption>A list of the users</TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead className="w-[200px]">Name</TableHead>
            <TableHead >E-mail</TableHead>
            <TableHead >Role</TableHead>
            <TableHead  >Action</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {users.map( ( user ) => (
            <TableRow key={user._id}>
              <TableCell className="text-left">{user.name}</TableCell>
              <TableCell className="text-left">{user.email}</TableCell>
              <TableCell className="text-left">{user.role}</TableCell>
              <TableCell className="text-left" onClick={() => deleteUser( user._id )}><MdDeleteForever className="text-red-500 text-xl cursor-pointer" /></TableCell>
            </TableRow>
          ) )}
        </TableBody>
      </Table>
    </div>
  )
}

export default withNavbarProvided( Dashboard )