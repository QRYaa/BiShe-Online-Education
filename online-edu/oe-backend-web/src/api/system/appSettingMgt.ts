import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsAppSetting/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsAppSetting/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsAppSetting/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsAppSetting/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsAppSetting/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsAppSetting/delete',
    data,
  });
}
