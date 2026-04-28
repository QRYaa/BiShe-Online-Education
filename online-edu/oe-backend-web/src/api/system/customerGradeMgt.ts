import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsCustomerGrade/page',
    params,
  });
}
export function listAll() {
  return request.get<any>({
    url: '/admin/bsCustomerGrade/listAll',
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsCustomerGrade/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsCustomerGrade/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsCustomerGrade/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsCustomerGrade/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsCustomerGrade/delete',
    data,
  });
}
