import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsCustomer/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsCustomer/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsCustomer/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsCustomer/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsCustomer/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsCustomer/delete',
    data,
  });
}
